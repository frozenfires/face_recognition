package com.ethink.face.utils;


import java.util.Comparator;

import com.ethink.face.bean.ImageBean;



/**
 * 自定义StoreComparator,实现Comparator接口,重写compare方法
 * 
 * @author 
 */
public class ImageComparator implements Comparator<ImageBean> {

    @Override
    public int compare(ImageBean o1, ImageBean o2) {
        int ret = 0;
        
        double sg = o2.getSimilar() - o1.getSimilar();
        if (sg != 0) {
            ret = sg > 0 ? 1 : -1;
        } else {
            // 店铺距离由近及远
            sg = (o1.getSimilar() - o2.getSimilar()) > 0 ? 1 : -1;
            if (sg != 0) {
                ret = sg > 0 ? 1 : -1;
            }
        }
        return ret;
    }

    
    
}