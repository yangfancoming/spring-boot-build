

package org.springframework.boot.autoconfigure.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JobLauncherCommandLineRunner}.
 *
 * @author Dave Syer
 * @author Jean-Pierre Bergamin
 */
public class JobLauncherCommandLineRunnerTests {

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	private JobLauncherCommandLineRunner runner;

	private JobExplorer jobExplorer;

	private JobBuilderFactory jobs;

	private StepBuilderFactory steps;

	private Job job;

	private Step step;

	@Before
	public void init() {
		this.context.register(BatchConfiguration.class);
		this.context.refresh();
		JobRepository jobRepository = this.context.getBean(JobRepository.class);
		JobLauncher jobLauncher = this.context.getBean(JobLauncher.class);
		this.jobs = new JobBuilderFactory(jobRepository);
		PlatformTransactionManager transactionManager = this.context
				.getBean(PlatformTransactionManager.class);
		this.steps = new StepBuilderFactory(jobRepository, transactionManager);
		Tasklet tasklet = (contribution, chunkContext) -> null;
		this.step = this.steps.get("step").tasklet(tasklet).build();
		this.job = this.jobs.get("job").start(this.step).build();
		this.jobExplorer = this.context.getBean(JobExplorer.class);
		this.runner = new JobLauncherCommandLineRunner(jobLauncher, this.jobExplorer);
		this.context.getBean(BatchConfiguration.class).clear();
	}

	@After
	public void closeContext() {
		this.context.close();
	}

	@Test
	public void basicExecution() throws Exception {
		this.runner.execute(this.job, new JobParameters());
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(1);
		this.runner.execute(this.job,
				new JobParametersBuilder().addLong("id", 1L).toJobParameters());
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(2);
	}

	@Test
	public void incrementExistingExecution() throws Exception {
		this.job = this.jobs.get("job").start(this.step)
				.incrementer(new RunIdIncrementer()).build();
		this.runner.execute(this.job, new JobParameters());
		this.runner.execute(this.job, new JobParameters());
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(2);
	}

	@Test
	public void retryFailedExecution() throws Exception {
		this.job = this.jobs.get("job")
				.start(this.steps.get("step").tasklet(throwingTasklet()).build())
				.incrementer(new RunIdIncrementer()).build();
		this.runner.execute(this.job, new JobParameters());
		this.runner.execute(this.job, new JobParameters());
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(1);
	}

	@Test
	public void retryFailedExecutionOnNonRestartableJob() throws Exception {
		this.job = this.jobs.get("job").preventRestart()
				.start(this.steps.get("step").tasklet(throwingTasklet()).build())
				.incrementer(new RunIdIncrementer()).build();
		this.runner.execute(this.job, new JobParameters());
		this.runner.execute(this.job, new JobParameters());
		// A failed job that is not restartable does not re-use the job params of
		// the last execution, but creates a new job instance when running it again.
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(2);
	}

	@Test
	public void retryFailedExecutionWithNonIdentifyingParameters() throws Exception {
		this.job = this.jobs.get("job")
				.start(this.steps.get("step").tasklet(throwingTasklet()).build())
				.incrementer(new RunIdIncrementer()).build();
		JobParameters jobParameters = new JobParametersBuilder().addLong("id", 1L, false)
				.addLong("foo", 2L, false).toJobParameters();
		this.runner.execute(this.job, jobParameters);
		this.runner.execute(this.job, jobParameters);
		assertThat(this.jobExplorer.getJobInstances("job", 0, 100)).hasSize(1);
	}

	private Tasklet throwingTasklet() {
		return (contribution, chunkContext) -> {
			throw new RuntimeException("Planned");
		};
	}

	@Configuration
	@EnableBatchProcessing
	protected static class BatchConfiguration implements BatchConfigurer {

		private ResourcelessTransactionManager transactionManager = new ResourcelessTransactionManager();

		private JobRepository jobRepository;

		private MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(
				this.transactionManager);

		public BatchConfiguration() throws Exception {
			this.jobRepository = this.jobRepositoryFactory.getObject();
		}

		public void clear() {
			this.jobRepositoryFactory.clear();
		}

		@Override
		public JobRepository getJobRepository() {
			return this.jobRepository;
		}

		@Override
		public PlatformTransactionManager getTransactionManager() {
			return this.transactionManager;
		}

		@Override
		public JobLauncher getJobLauncher() {
			SimpleJobLauncher launcher = new SimpleJobLauncher();
			launcher.setJobRepository(this.jobRepository);
			launcher.setTaskExecutor(new SyncTaskExecutor());
			return launcher;
		}

		@Override
		public JobExplorer getJobExplorer() throws Exception {
			return new MapJobExplorerFactoryBean(this.jobRepositoryFactory).getObject();
		}

	}

}