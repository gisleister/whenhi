package com.whenhi.hi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王雷 on 2017/1/17.
 */

public class DiscoveryData implements Serializable {
    List<User> topUsers;
    List<Feed> topShareFeeds;
    List<Feed> topHotFeeds;

    public List<User> getTopUsers() {
        return topUsers;
    }

    public void setTopUsers(List<User> topUsers) {
        this.topUsers = topUsers;
    }

    public List<Feed> getTopShareFeeds() {
        return topShareFeeds;
    }

    public void setTopShareFeeds(List<Feed> topShareFeeds) {
        this.topShareFeeds = topShareFeeds;
    }

    public List<Feed> getTopHotFeeds() {
        return topHotFeeds;
    }

    public void setTopHotFeeds(List<Feed> topHotFeeds) {
        this.topHotFeeds = topHotFeeds;
    }
}