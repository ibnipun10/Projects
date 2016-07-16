DROP TABLE IF EXISTS CONFIG_SCHEMA_TBL;
DROP TABLE IF EXISTS CONFIG_VALUE_TBL;


CREATE TABLE IF NOT EXISTS CONFIG_SCHEMA_TBL
(
    NodeID          INTEGER PRIMARY KEY,
    NodeValue       VARCHAR(256),
    ParentNodeID    INTEGER,
    Time_1          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = 'InnoDB';

/*
ParentNodeID should be foreign key of NodeID in the same table
Add RouterIP or ID later whenever require.
*/


CREATE TABLE IF NOT EXISTS CONFIG_VALUE_TBL
(
    NodeID          INTEGER PRIMARY KEY,
    NodeValue       VARCHAR(256),
    ParentNodeID    INTEGER,
    SectionID       INTEGER,
    Time_1          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = 'InnoDB';
