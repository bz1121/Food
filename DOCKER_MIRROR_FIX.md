# 🔧 Docker镜像拉取问题修复

**问题**: 无法从Docker Hub拉取镜像  
**原因**: 服务器网络限制

---

## 🎯 解决方案

### 方案1: 使用阿里云镜像加速器（推荐）

**在服务器执行**:

```bash
# 配置阿里云镜像源
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": [
    "https://mirror.ccs.tencentyun.com",
    "https://docker.m.daocloud.io",
    "https://dockerproxy.com",
    "https://docker.nju.edu.cn"
  ]
}
EOF

# 重启Docker
sudo systemctl daemon-reload
sudo systemctl restart docker

# 验证配置
sudo docker info | grep -A 5 "Registry Mirrors"
```

---

### 方案2: 手动拉取基础镜像

```bash
# 尝试拉取基础镜像
sudo docker pull mysql:8.0
sudo docker pull redis:7-alpine

# 如果还是超时，使用国内源直接拉取
sudo docker pull registry.cn-hangzhou.aliyuncs.com/library/mysql:8.0
sudo docker pull registry.cn-hangzhou.aliyuncs.com/library/redis:7-alpine

# 然后打标签
sudo docker tag registry.cn-hangzhou.aliyuncs.com/library/mysql:8.0 mysql:8.0
sudo docker tag registry.cn-hangzhou.aliyuncs.com/library/redis:7-alpine redis:7-alpine
```

---

### 方案3: 使用简化的docker-compose

**创建简化版**:

```bash
cd /opt/Food

# 使用开发版docker-compose（更简单）
sudo docker-compose up -d

# 而不是docker-compose.prod.yml
```

---

## 🚀 推荐执行

**立即在服务器执行**:

```bash
# 1. 重新配置镜像源
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": [
    "https://mirror.ccs.tencentyun.com",
    "https://dockerproxy.com"
  ]
}
EOF

# 2. 重启Docker
sudo systemctl daemon-reload
sudo systemctl restart docker

# 3. 使用简化的docker-compose
cd /opt/Food
sudo docker-compose up -d

# 4. 查看状态
sudo docker ps
```

---

## 💡 如果还是失败

**使用宝塔面板** (推荐):

```bash
# 安装宝塔面板
wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
sudo bash install.sh

# 等待安装完成
# 访问 http://14.103.168.111:8888
# 在面板中可视化管理Docker
```

**宝塔面板优势**:
- 可视化界面
- 内置Docker管理
- 一键配置镜像源
- 监控和日志

---

**执行上面的命令，使用简化版docker-compose！** 🚀

