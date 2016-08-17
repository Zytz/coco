package com.ms.coco.model;

/**
 * @author wanglin/netboy
 * @version 创建时间：2016年8月15日 下午7:02:22
 * @func
 */
public class ServiceConf {
    private String serviceName;
    private String groupName;
    private String heartCenterName;
    private String registerUrl;
    private ServerNode serverNode;
    private Long redisKeyLiveTimeInMillisecond = 2000L;
    private Long pingSleepTimeInMillisecond = 1500L;

    public ServiceConf() {

    }

    public ServiceConf(String serviceName, String groupName, String heartCenterName, String registerUrl,
            ServerNode serverNode, Long redisKeyLiveTimeInMillisecond, Long pingSleepTimeInMillisecond) {
        super();
        this.serviceName = serviceName;
        this.groupName = groupName;
        this.heartCenterName = heartCenterName;
        this.registerUrl = registerUrl;
        this.serverNode = serverNode;
        this.redisKeyLiveTimeInMillisecond = redisKeyLiveTimeInMillisecond;
        this.pingSleepTimeInMillisecond = pingSleepTimeInMillisecond;
    }

    public Long getPingSleepTimeInMillisecond() {
        return pingSleepTimeInMillisecond;
    }


    public ServiceConf setPingSleepTimeInMillisecond(Long pingSleepTimeInMillisecond) {
        this.pingSleepTimeInMillisecond = pingSleepTimeInMillisecond;
        return this;
    }


    public ServerNode getServerNode() {
        return serverNode;
    }

    public ServiceConf setServerNode(ServerNode serverNode) {
        this.serverNode = serverNode;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceConf setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public ServiceConf setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public ServiceConf setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
        return this;
    }

    public String getRedisKeyLiveTimeInMillisecond() {
        return redisKeyLiveTimeInMillisecond + "";
    }

    public ServiceConf setRedisKeyLiveTimeInMillisecond(Long redisKeyLiveTimeInMillisecond) {
        this.redisKeyLiveTimeInMillisecond = redisKeyLiveTimeInMillisecond;
        return this;
    }

    public String getHeartCenterName() {
        return heartCenterName;
    }

    public ServiceConf setHeartCenterName(String heartCenterName) {
        this.heartCenterName = heartCenterName;
        return this;
    }

}
