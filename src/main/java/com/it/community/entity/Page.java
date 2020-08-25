package com.it.community.entity;

/**
 * 封装分页相关的信息.
 */
public class Page {

    // 当前页码(前端传入)
    private int current = 1;
    // 显示上限(前端传入)
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径(因为前端页码要有路径,如果让前端自己拼接的话不方便,这里为了方便前端)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        //防止用户传入负数和0
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        //防止用户传入不合法数据
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     */
    public int getOffset() {
        // current * limit - limit
        // 当前页号 * 每页多少 - 每页多少 = 当前页起始行号
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal() {
        // rows / limit
        //数据总数 / 每页多少 = 总页数
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            //不能整除 +1页,因为rows/limit向下取整了
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页号
     * 比如当前页为8 起始页为6 结束页为10
     */
    public int getFrom() {
        int from = current - 2;
        //如果当前页为1 from就为负数了
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页号
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
