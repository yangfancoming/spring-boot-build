

def foo() {
	"Foo"
}
 
command("foo") { options ->
	def foo = foo()
	println "Hello ${foo} ${options.nonOptionArguments()}: ${options.has('foo')} ${options.valueOf('bar')}"
}
