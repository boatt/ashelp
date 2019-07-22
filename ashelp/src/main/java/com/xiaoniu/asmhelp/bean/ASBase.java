package com.xiaoniu.asmhelp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * <p>
 *
 * @author: ZhouTao
 * Date: 2019/7/8
 * Copyright: Copyright (c) 2016-2020
 * Company: @小牛科技
 * Update
 */
public class ASBase implements Serializable {

    /**
     * 行为延迟时间
     */
    public int delay_time;
    /**
     * 大分类ID
     */
    public int type_id;
    /**
     * 描述
     */
    public String describe;
    public ASIntentBean intent;
    public List<ASStepBean> step;


}
