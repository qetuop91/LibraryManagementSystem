package com.feliks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feliks.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
