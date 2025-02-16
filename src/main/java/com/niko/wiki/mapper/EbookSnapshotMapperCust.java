package com.niko.wiki.mapper;

import com.niko.wiki.resp.StatisticResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookSnapshotMapperCust {

    public void genSnapshot();

    public List<StatisticResp> getStatistic();

    public List<StatisticResp> get30Statistic();

}
