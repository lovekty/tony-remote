namespace java me.tonyirl.tremote.core.test.thrift

struct DemoStruct {
1:optional string key;
2:optional string name;
3:optional i32 code;
}

service DemoService {
DemoStruct queryByKey(1:required string key);
}