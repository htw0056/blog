# git命令

### 1. git help

| 命令            | 作用          |
| ------------- | ----------- |
| git help      | 查看帮助        |
| git help -a   | 查看帮助，包含各种命令 |
| git help [命令] | 查看某个命令的帮助   |


### 2. git config

| 命令                                       | 作用                   |
| ---------------------------------------- | -------------------- |
| git config user.name "your name"         | 设置局部的用户名称            |
| git config user.email "your email"       | 设置局部的用户邮箱            |
| git config --global user.name "your name" | 设置全局(用户级别)的用户名称      |
| git config --local user.email "your email" | 设置当前项目的用户邮箱(项目级别,默认) |
| git config --unset xxx                   | 删除变量xxx              |
| git config --global --unset xxx          | 删除全局变量xxx            |
| git config --list                        | 查看变量列表               |


### 3. git init

| 命令       | 作用      |
| -------- | ------- |
| git init | 初始化一个仓库 |


### 4. git status

| 命令         | 作用      |
| ---------- | ------- |
| git status | 查看仓库的状态 |

### 5.git add

| 命令          | 作用          |
| ----------- | ----------- |
| git add 文件名 | 将文件提交到暂存区   |
| git add -a  | 将所有文件提交到暂存区 |

### 6. git commit

| 命令                 | 作用            |
| ------------------ | ------------- |
| git commit         | 将暂存区的内容提交到分支上 |
| git commit -m "说明" | 将暂存区的内容提交到分支上 |

### 7. git log

| 命令                | 作用                        |
| ----------------- | ------------------------- |
| git log           | 查看历史记录                    |
| git log --oneline | 查看历史记录，一条记录一行             |
| git reflog        | Manage reflog information |

### 8. git mv

| 命令         | 作用                                       |
| ---------- | ---------------------------------------- |
| git mv a b | 将**tracked**的文件a改名为b,本地文件a也会改名为b；<br />等同于三个操作:`mv a b`,`git rm a`,`git add b` |

### 9. git rm

| 命令                | 作用               |
| ----------------- | ---------------- |
| git rm --cached a | 将暂存区的文件a删除，本地不删除 |
| git rm -f a       | 将暂存区的文件a删除，本地也删除 |


### 10. git revert

作用：撤销一个commit

| 命令                | 作用               |
| ----------------- | ---------------- |
| git revert HEAD   | 撤销当前版本           |
| git revert HEAD^  | 撤销一个版本，^一个代表一个版本 |
| git revert HEAD~n | 撤销n个版本，n代表数字     |


### 11. git reset

revert和reset的区别：  
revert(中文意思是：反转)，撤销一个commit,仅仅影响指定的commit，当前HEAD指针和 revert commit中间的commit不会受到影响。会添加一个提交记录，HEAD指针后移  
reset（中文意思是：重置），直接回到某次提交，中间的提交内容也会受到影响。不会添加提交记录，HEAD指针移到指定commit处


| 命令                      | 作用         |
| ----------------------- | ---------- |
| git reset HEAD^ --soft  | 软重置        |
| git reset HEAD^ --mixed | 混合重置       |
| git reset HEAD^ --hard  | 硬重置        |
| git reset 版本号           | 默认为--mixed |

### 12. git branch


| 命令                     | 作用           |
| ---------------------- | ------------ |
| git branch             | 查看分支         |
| git branch dev         | 创建dev分支      |
| git branch -d dev      | 删除dev分支      |
| git branch -m dev dev2 | 修改分支dev为dev2 |
| git brach -a           | 查看本地和远程所有分支  |



### 13. git checkout

| 命令                               | 作用                          |
| -------------------------------- | --------------------------- |
| git checkout dev                 | 切换到dev分支                    |
| git checkout -- 文件a              | 将文件a还原                      |
| git checkout -b dev [origin/dev] | 创建并切换到dev分支[并直接关联到远程的dev分支] |


### 14. git merge

| 命令                             | 作用                        |
| ------------------------------ | ------------------------- |
| git merge dev                  | 将dev合并到当前分支               |
| git merge dev --on-ff -m "xxx" | 将dev合并到当前分支,并增加一个commit信息 |

### 15. git diff

| 命令                   | 作用              |
| -------------------- | --------------- |
| git diff             | 查看当前仓库的修改       |
| git diff 文件名a        | 查看文件a的修改        |
| git diff master..dev | 查看master和dev的区别 |




### 16. git stash

| 命令                 | 作用         |
| ------------------ | ---------- |
| git stash          | 封存当前的修改    |
| git stash pop      | pop出前一次的封存 |
| git stash apply 版本 | pop出某一次的封存 |

 

### 17. 远程库相关命令

| 命令                                                  | 作用                                             |
| ----------------------------------------------------- | ------------------------------------------------ |
| git clone git@github.com:htw0056/blog.git             | 克隆远程的库                                     |
| git remote -v                                         | 查看本地和远程库的关联信息                       |
| git remote add origin git@github.com:htw0056/blog.git | 远程库关联为origin                               |
| git remote remove origin                              | 删除已关联的远程库origin                         |
| git branch --set-upstream dev origin/dev              | 关联本地dev和远端的dev分支                       |
| git push [-u] origin master                           | 推送到远程的origin/master分支[第一次使用加上 -u] |
| git pull origin master                                | 更新并merge远程origin/master分支                 |
| git fetch origin master                               | 更新远程origin/master分支,但不merge              |


