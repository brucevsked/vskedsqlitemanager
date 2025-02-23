PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS "roles";
CREATE TABLE "roles" (
  "ID" INTEGER NOT NULL,
  "name" TEXT,
  "describe" TEXT,
  PRIMARY KEY ("ID")
);

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO "roles" VALUES (1, 'root', 'this is root');
INSERT INTO "roles" VALUES (2, 'user', 'only has limit right');
INSERT INTO "roles" VALUES (3, 'guest', 'you are guest');

-- ----------------------------
-- Table structure for userRoles
-- ----------------------------
DROP TABLE IF EXISTS "userRoles";
CREATE TABLE "userRoles" (
  "userId" INTEGER NOT NULL,
  "roleId" INTEGER NOT NULL,
  PRIMARY KEY ("userId", "roleId"),
  CONSTRAINT "userIdFK" FOREIGN KEY ("userId") REFERENCES "users" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "roleIdFK" FOREIGN KEY ("roleId") REFERENCES "roles" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- ----------------------------
-- Records of userRoles
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
  "ID" integer,
  "userName" text,
  "age" INTEGER,
  "password" TEXT
);

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO "users" VALUES (1, 'user1', 18, 12345);
INSERT INTO "users" VALUES (2, 'user2', 19, 'good');
INSERT INTO "users" VALUES (2, 'user3', 20, 'hello');

PRAGMA foreign_keys = true;
