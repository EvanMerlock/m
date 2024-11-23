fn main() {
    let cfile_bytes = include_bytes!("../../java-agent/src/main/java/dev/merlock/helloworld/HelloWorld.class");

    let class = classfile_parser::class_parser(cfile_bytes).expect("failed to parse classfile!");

    println!("{:#?}", class.1);
}
