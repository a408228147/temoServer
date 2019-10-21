package com.creams.temo.service.database;

import com.creams.temo.entity.database.request.DatabaseRequest;
import com.creams.temo.entity.database.response.DatabaseResponse;
import com.creams.temo.mapper.database.DatabaseMapper;
import com.creams.temo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseMapper databaseMapper;

    /**
     * 查询所有Database
     * @return
     */
    public List<DatabaseResponse> queryAllDatabase(){
        List<DatabaseResponse> databaseResponses = databaseMapper.queryDatabase();
        return databaseResponses;
    }

    /**
     * 查询database详情
     * @param dbId
     * @return
     */
    public DatabaseResponse queryDatabaseById(String dbId){

        DatabaseResponse databaseResponse = databaseMapper.queryDatabaseById(dbId);
        return databaseResponse;
    }

    /**
     * 新增database
     * @param databaseRequest
     * @return
     */
    @Transactional
    public String addDatabase(DatabaseRequest databaseRequest){
        String dbId = StringUtil.uuid();
        databaseRequest.setDbId(dbId);
        databaseMapper.addDatabase(databaseRequest);
        return dbId;
    }

    /**
     * 修改database
     * @param databaseRequest
     * @return
     */
    @Transactional
    public boolean updateDatabaseById(DatabaseRequest databaseRequest){
        boolean result = true;
        databaseMapper.updateDatabaseById(databaseRequest);
        return result;

    }

    /**
     * 删除数据库信息
     * @param dbId
     * @return
     */
    @Transactional
    public boolean deleteDabaseById(String dbId){
        boolean result = true;
        databaseMapper.deteleDatabaseById(dbId);
        return result;
    }

}
