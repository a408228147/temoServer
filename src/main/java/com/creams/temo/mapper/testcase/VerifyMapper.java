package com.creams.temo.mapper.testcase;

import com.creams.temo.entity.testcase.response.VerifyResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VerifyMapper {
    List<VerifyResponse> queryVerify();

    boolean addVerify(VerifyResponse verifyResponse);

    boolean updateVerifyById(VerifyResponse verifyResponse);

    @Delete("delete from verify where verify_id = #{verify_id}")
    boolean deleteVerify(@Param("verify_id") String verifyId);
}
