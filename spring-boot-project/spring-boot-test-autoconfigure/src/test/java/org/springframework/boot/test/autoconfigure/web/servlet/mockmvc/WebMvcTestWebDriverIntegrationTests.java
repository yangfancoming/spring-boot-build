

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link WebMvcTest} with {@link WebDriver}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebMvcTestWebDriverIntegrationTests {

	private static WebDriver previousWebDriver;

	@Autowired
	private WebDriver webDriver;

	@Test
	public void shouldAutoConfigureWebClient() {
		this.webDriver.get("/html");
		WebElement element = this.webDriver.findElement(By.tagName("body"));
		assertThat(element.getText()).isEqualTo("Hello");
		WebMvcTestWebDriverIntegrationTests.previousWebDriver = this.webDriver;
	}

	@Test
	public void shouldBeADifferentWebClient() {
		this.webDriver.get("/html");
		WebElement element = this.webDriver.findElement(By.tagName("body"));
		assertThat(element.getText()).isEqualTo("Hello");
		try {
			ReflectionTestUtils.invokeMethod(previousWebDriver, "getCurrentWindow");
			fail("Did not call quit()");
		}
		catch (NoSuchWindowException ex) {
			// Expected
		}
		assertThat(previousWebDriver).isNotNull().isNotSameAs(this.webDriver);
	}

}
