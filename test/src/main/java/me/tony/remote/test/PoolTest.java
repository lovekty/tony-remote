package me.tony.remote.test;

import me.tony.remote.core.NodeInfo;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author tony.zhuby
 * @date 2020/5/24
 */
public class PoolTest {

    public static void main(String[] args) {
//        ObjectPool<NodeInfo> pool = new GenericObjectPool<>(new BasePooledObjectFactory<NodeInfo>() {
//            @Override
//            public NodeInfo create() throws Exception {
//                return NodeInfo.builder().build();
//            }
//
//            @Override
//            public PooledObject<NodeInfo> wrap(NodeInfo obj) {
//                return new DefaultPooledObject<>(obj);
//            }
//        });
    }
}
