package com.feliks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feliks.entity.Borrow;
import com.feliks.po.BorrowReturnCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Borrow)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-09 15:16:21
 */
@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {
    List<BorrowReturnCountPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);
}
