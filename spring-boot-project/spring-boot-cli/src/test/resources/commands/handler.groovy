//
//
//package org.test.command
//
//import joptsimple.OptionSet
//@Grab("org.eclipse.jgit:org.eclipse.jgit:2.3.1.201302201838-r")
//import org.eclipse.jgit.api.Git
//import org.springframework.boot.cli.command.options.OptionHandler
//
//
//class TestCommand extends OptionHandler {
//
//	String name = "foo"
//
//	void options() {
//		option "foo", "Foo set"
//	}
//
//	void run(OptionSet options) {
//		// Demonstrate use of Grape.grab to load dependencies before running
//		println "Clean: " + Git.open(".." as File).status().call().isClean()
//		println "Hello ${options.nonOptionArguments()}: ${options.has('foo')}"
//	}
//
//}
