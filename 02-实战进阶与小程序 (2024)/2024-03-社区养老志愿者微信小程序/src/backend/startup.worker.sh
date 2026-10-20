#!/usr/bin/env bash
# 社区养老志愿者微信小程序后端启动脚本
# 1) 确保 MariaDB/MySQL 可用并导入 schema + seed
# 2) mvn spring-boot:run (端口 19000)
set -e
cd "$(dirname "$0")"

echo "[1/3] 检查数据库..."
mysql -h127.0.0.1 -uelderapp -pelderapp123 -e "SELECT 1" community_elder_care >/dev/null 2>&1 || {
  echo "导入 schema/seed ..."
  mysql -uroot -proot123 < sql/schema.sql
  mysql -uroot -proot123 < sql/seed.sql
}

echo "[2/3] 构建并启动 SpringBoot (端口 19000)..."
export MAVEN_OPTS="-Xmx512m"
nohup mvn -q -DskipTests spring-boot:run > /tmp/elder-backend.log 2>&1 &
echo "PID=$!"

echo "[3/3] 健康检查..."
for i in $(seq 1 60); do
  if curl -sf http://127.0.0.1:19000/api/admin/stats >/dev/null 2>&1; then
    echo "Backend ready: http://127.0.0.1:19000"
    exit 0
  fi
  sleep 2
done
echo "启动超时,请查看 /tmp/elder-backend.log"
exit 1
