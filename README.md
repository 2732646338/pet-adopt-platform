# 宠物领养平台 pet-adopt-platform
> 基于SpringBoot + Vue3 + MySQL + Redis的宠物领养管理系统

## 项目介绍
本项目分为前端和后端，实现宠物展示、筛选、宠物详情、收藏、领养申请、管理员审核等功能。
- 用户：注册登录、浏览宠物、提交领养申请、收藏宠物，个人中心管理资料
- 管理员：宠物信息维护、领养申请审核管理

## 技术栈
**后端**
Java、SpringBoot、MyBatis-Plus、MySQL、Redis、JWT、SpringSecurity

**前端**
Vue3、Vite、Element Plus、Axios、Pinia、Vue-Router

## 功能模块
1. 用户模块：登录、注册，token身份认证
2. 宠物模块：首页轮播、宠物筛选、宠物详情、收藏
3. 领养模块：提交领养申请，留言
4. 个人中心：修改头像、个人资料、我的收藏、我的申请
5. 管理员后台：宠物管理、领养申请审核

## 环境部署
### 后端
1. MySQL导入sql脚本（项目中提供pet.sql）
2. 修改application-example.yml数据库配置
3. 启动SpringBoot项目，默认端口8080

### 前端
```bash
# 安装依赖
npm install
# 启动开发环境
npm run dev
