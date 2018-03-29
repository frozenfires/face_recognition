-------------------------------------------------------------
--                 人脸识别平台系统管理初始化脚本               --
-------------------------------------------------------------

--创建用户信息表
CREATE TABLE ETF_USER(
    -- 用户id
	ID VARCHAR PRIMARY KEY  NOT NULL,
    -- 用户密码
	PASSWORD VARCHAR NOT NULL,
    -- 角色
    ROLE VARCHAR,
    -- 状态
	STATUS VARCHAR,
    -- 更新时间
	UPDATETIME DATETIME
);
-- 初始化超级用户信息
insert into ETF_USER(ID, PASSWORD, ROLE, STATUS, UPDATETIME) 
    values('admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'admin', 'active', datetime('now','localtime'));

--创建token表
CREATE TABLE ETF_TOKEN ( 
    ID VARCHAR PRIMARY KEY NOT NULL,
    USERID NOT NULL,
    APPNAME VARCHAR,
    SDKEY VARCHAR,
    STATUS  VARCHAR,
    UPDATETIME  DATETIME
);


