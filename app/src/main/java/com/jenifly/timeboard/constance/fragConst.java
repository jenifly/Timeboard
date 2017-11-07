package com.jenifly.timeboard.constance;




import com.jenifly.timeboard.fragment.mainFrag;
import com.jenifly.timeboard.info.BaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jenifly on 2017/6/23.
 */
public class fragConst {

   public static List<mainFrag>  fraglist = new ArrayList<>();  //fraglist和fraghashcode成对出现

   public static  int page_interval = 50;  //界面之间的间隔

   public static  int new_mainfrag_count = 0;  // mainFrag类构造函数被调用的次数

}
