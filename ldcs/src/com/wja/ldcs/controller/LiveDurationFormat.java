package com.wja.ldcs.controller;

import com.wja.base.util.PoiExcelUtil.DataFormat;

public class LiveDurationFormat implements DataFormat
{
    
    @Override
    public String format(Object v)
    {
        if (v != null)
        {
            int ld = (Integer)v;
            if (ld > 0)
            {
                StringBuilder s = new StringBuilder();
                int h = ld / 60;
                if (h > 0)
                {
                    s.append(h + "小时");
                }
                int m = ld % 60;
                if (m > 0)
                {
                    s.append(m + "分");
                }
                return s.toString();
            }
        }
        return "";
    }
    
}
