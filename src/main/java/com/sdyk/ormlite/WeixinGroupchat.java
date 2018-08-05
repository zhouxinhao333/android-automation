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
@DatabaseTable(tableName = "weixin_groupchat")

public class WeixinGroupchat {
    private static final Logger logger = LogManager.getLogger(WeixinMoment.class.getName());

    @DatabaseField(dataType = DataType.INTEGER, index = true, generatedId = true)
    public int id;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String device_id;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String user_name;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String user_id;

    @DatabaseField(dataType = DataType.STRING, width = 128)
    public String group_name;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] content;

    @DatabaseField(dataType = DataType.STRING, width = 32)
    public String type;

    @DatabaseField(dataType = DataType.DATE)
    public Date insert_time;

    public WeixinGroupchat() {
    }

    public WeixinGroupchat(String device_id, String user_id, String user_name) {
        this.device_id = device_id;
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public boolean insert() throws Exception {
        Dao<WeixinGroupchat, String> dao = DaoManager.getDao(WeixinGroupchat.class);
        if (dao.create(this) == 1) {
            return true;
        }
        return false;
    }

    public boolean update() throws Exception {
        Dao<WeixinGroupchat, String> dao = DaoManager.getDao(WeixinGroupchat.class);
        if (dao.update(this) == 1) {
            return true;
        }
        return false;
    }

    public static WeixinGroupchat getById(int id) throws Exception {
        Dao<WeixinGroupchat, String> dao = DaoManager.getDao(WeixinGroupchat.class);
        return dao.queryForId(String.valueOf(id));
    }
}
