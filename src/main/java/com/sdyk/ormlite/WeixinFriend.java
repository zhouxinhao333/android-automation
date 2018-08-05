package com.sdyk.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import one.rewind.db.DBName;
import one.rewind.db.DaoManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@DBName(value = "android_auto")
@DatabaseTable(tableName = "weixin_friends")
public class WeixinFriend {

    private static final Logger logger = LogManager.getLogger(WeixinFriend.class.getName());

    @DatabaseField(dataType = DataType.INTEGER, index = true, generatedId = true)
    public int id;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String device_id;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String user_name;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String user_id;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String friend_name;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String friend_id;

    @DatabaseField(dataType = DataType.DATE)
    public Date insert_time = new Date();

    @DatabaseField(dataType = DataType.DATE)
    public Date update_time = new Date();

    public WeixinFriend() {
    }

    public WeixinFriend(String device_id, String user_id, String user_name, String friend_id, String friend_name) {
        this.device_id = device_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.friend_id = friend_id;
        this.friend_name = friend_name;
    }

    public boolean insert() throws Exception {
        Dao<WeixinFriend, String> dao = DaoManager.getDao(WeixinFriend.class);
        if (dao.create(this) == 1) {
            return true;
        }
        return false;
    }

    public boolean update() throws Exception {
        Dao<WeixinFriend, String> dao = DaoManager.getDao(WeixinFriend.class);
        if (dao.update(this) == 1) {
            return true;
        }
        return false;
    }

    public static WeixinFriend getById(int id) throws Exception {
        Dao<WeixinFriend, String> dao = DaoManager.getDao(WeixinFriend.class);
        return dao.queryForId(String.valueOf(id));
    }
}
