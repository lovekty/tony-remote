namespace java me.tony.remote.test.demo

struct DemoModel {
1:required string id;
2:optional string name;
3:optional i32 price;
}

service DemoService {
DemoModel queryById(1:string id)
}