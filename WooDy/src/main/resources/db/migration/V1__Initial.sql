CREATE TABLE T_FEED (
	C_ID INT AUTO_INCREMENT PRIMARY KEY,
	C_RSS_SOURCE VARCHAR(20),
	C_TITLE VARCHAR(256),
	C_DESCRIPTION VARCHAR(4096),
	C_LINK VARCHAR(256),
	C_GUID VARCHAR(256),
	C_PUBLISHED DATETIME,
	C_CATEGORY VARCHAR(20),
	C_SUB_CATEGORY VARCHAR(20),
	C_CREATED DATETIME,
	CONSTRAINT UC_GUID UNIQUE (C_GUID)
);

CREATE TABLE T_IP_HASH (
	C_ID INT AUTO_INCREMENT PRIMARY KEY,
	C_IP_HASH VARCHAR(64),
	CONSTRAINT UC_IP_HASH UNIQUE (C_IP_HASH)
);

CREATE TABLE J_FEED_IP_HASH (
	C_FEED_ID INT NOT NULL,
    C_IP_HASH_ID INT NOT NULL,
    CONSTRAINT PK_KEY PRIMARY KEY (C_FEED_ID,C_IP_HASH_ID),
    CONSTRAINT FK_FEED FOREIGN KEY (C_FEED_ID) REFERENCES T_FEED(C_ID),
    CONSTRAINT FK_IP_HASH FOREIGN KEY (C_IP_HASH_ID) REFERENCES T_IP_HASH(C_ID)
);


