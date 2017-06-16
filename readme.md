使用Netty作为底层通信框架 ,写的一个简单的`RPCdemo`练习 ,参考自[开源中国-黄勇](https://my.oschina.net/huangyong/blog/361751)
#### 开源组件
- netty
- spring-context
- protostuff
- zookeeper
- cglib
- jackson

#### 运行
- 安装运行`zookeeper`,配置`rpc.properties`中的端口参数
- 启动运行`ServerTest`
- 启动运行`ClientTest`

#### 耗时统计示例
```
StopWatch '测试结果': running time (millis) = 1018
-----------------------------------------
ms     %     Task name
-----------------------------------------
00384  038%  第1次
00079  008%  第2次
00071  007%  第3次
00071  007%  第4次
00071  007%  第5次
00069  007%  第6次
00105  010%  第7次
00067  007%  第8次
00051  005%  第9次
00050  005%  第10次
```