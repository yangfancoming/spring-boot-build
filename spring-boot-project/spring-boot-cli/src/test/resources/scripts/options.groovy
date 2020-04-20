

command("foo") {
	options {
		option "foo", "Some foo description" withOptionalArg()
		option "bar", "Some bar" withOptionalArg()
	}
	run { options -> println "Hello ${options.nonOptionArguments()}: ${options.has('foo')} ${options.valueOf('bar')}" }
}
