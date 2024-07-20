package com.airport.ape.user;

import java.util.*;

public class SnowflakeIdWorker {
    /** 开始时间截  */
    private final long twepoch = 1420041600000L;

    /** 平台ID所占的位数 */
    private final long workerIdBits = 5L;

    /** 服务ID所占的位数 */
    private final long datacenterIdBits = 5L;

    /** 支持的最大平台ID，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大服务ID，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 随机数在id中占的位数 */
    private final long sequenceBits = 12L;

    /** 平台ID向左移12位 */
    private final long workerIdShift = sequenceBits;

    /** 服务ID向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 平台ID(0~31) */
    private long workerId;

    /** 服务ID(0~31) */
    private long datacenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
    /** 毫秒内随机数 */
    private long sequenceNum = 0L;

    private List<Integer> mylist = new ArrayList<>();

    private Random rd = new Random();

    private long times = System.currentTimeMillis();//记录定时器当前时间

    private long hours = 1000 * 60 * 60;//定时1小时从新生成随机数

    private int list_scope = 3000;
    private int list_total = 4094;


    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param workerId 平台ID编号数字 (0~31)
     * @param datacenterId 微服务ID编号数字 (0~31)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;

    }

    // 生成指定范围的随机数字【不重复】
    protected void randomNum(int scope, int total) {
        mylist = new ArrayList<>();
        while (mylist.size() < scope) {
            int myNum = rd.nextInt(total);
            if (!mylist.contains(myNum += 1)) { // 判断容器中是否包含指定的数字
                mylist.add(myNum); // 往集合里面添加数据。
            }
        }
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //定时刷新随机数集合或者初始化随机数集合
        if(mylist.size() == 0 || (times - timeGen()) >= times){
            randomNum(list_scope, list_total);
        }

        //如果是同一时间生成的，则进行毫秒随机数最后无序号生成
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if(sequence >= list_scope){
                sequence = 0L;
            }
//            System.out.println("+"+sequence);
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        sequenceNum = (long) mylist.get(Integer.parseInt(sequence+"")) & sequenceMask;
        //上次生成ID的时间截
        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequenceNum;
    }


    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /** 测试 */
    public static void main(String[] args) {
        //获取开始时间
        long startTime=System.currentTimeMillis();
        long key = 0L;
        long key1 = 0L;
        //实例化随机ID生成的方法
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);
        SnowflakeIdWorker idWorker1 = new SnowflakeIdWorker(1, 2);
        Map<Long,Integer> map = new HashMap<>();
        for (int i=0;i< 2000000 ;i++){
            //获取随机KEY
            key =idWorker.nextId();
            key1 =idWorker1.nextId();
            //打印前100个
            if(i<=100){
                System.out.println("KEY："+key);
                System.out.println("KEY："+key1);
            }
            if(map.get(key1)!=null){
                System.out.println("重复key："+key);
            }
            if(map.get(key)!=null){
                System.out.println("重复key："+key);
            }
            map.put(key,1);
            map.put(key1,1);
        }
        //结束时间
        long endTime=System.currentTimeMillis();
        long total = endTime - startTime;
        System.out.println("耗时时间" + total +"ms");
        System.out.println(map.size());
    }
}
