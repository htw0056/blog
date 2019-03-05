# git merge 与git revert所产生的问题

当执行`git merge `操作后再对合并节点进行`git revert`操作，此后再对相应的两条分支进行操作就会产生意想不到的问题。本文意在对此类情况进行讨论分析。



## 1. 初始化

```shell
$ git init	# 初始化仓库
Initialized empty Git repository in /Users/htw/Documents/tmp/test/.git/

$ touch master_1	# 在master创建文件master_1

$ git add .	

$ git commit -m 'add master_1'	# 提交
[master (root-commit) 074af34] add master_1
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 master_1

$ git lg		# 查看log
* 074af34 - (HEAD -> master) add master_1 (2 seconds ago) <htw112896>

$ git checkout -b dev	# 新建并切换到dev分支
Switched to a new branch 'dev'

$ touch dev_1		# 在dev创建文件dev_1

$ git add .

$ git commit -m 'add dev_1'	# 提交
[dev 1f73f04] add dev_1
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 dev_1

$ touch dev_2		# 在dev创建文件dev_2

$ git add .

$ git commit -m 'add dev_2'		# 提交
[dev 99cd6db] add dev_2
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 dev_2

$ git checkout master		# 切换到master分支
Switched to branch 'master'

$ git merge --no-ff dev		# --no-ff 合并dev分支
Merge made by the 'recursive' strategy.
 dev_1 | 0
 dev_2 | 0
 2 files changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 dev_1
 create mode 100644 dev_2

$ git lg			# 合并后，log
*   3463ae0 - (HEAD -> master) Merge branch 'dev' (3 seconds ago) <htw112896>
|\
| * 99cd6db - (dev) add dev_2 (59 seconds ago) <htw112896>
| * 1f73f04 - add dev_1 (73 seconds ago) <htw112896>
|/
* 074af34 - add master_1 (2 minutes ago) <htw112896>

$ git revert 3463ae0  -m 1			# revert dev的合并
[master f60e686] Revert "Merge branch 'dev'"
 2 files changed, 0 insertions(+), 0 deletions(-)
 delete mode 100644 dev_1
 delete mode 100644 dev_2
 
 $ git lg			# log情况
* f60e686 - (HEAD -> master) Revert "Merge branch 'dev'" (15 seconds ago) <htw112896>
*   3463ae0 - Merge branch 'dev' (6 minutes ago) <htw112896>
|\
| * 99cd6db - (dev) add dev_2 (7 minutes ago) <htw112896>
| * 1f73f04 - add dev_1 (7 minutes ago) <htw112896>
|/
* 074af34 - add master_1 (8 minutes ago) <htw112896>
```

简单介绍一下测试初始环境：创建dev分支，然后合并到master分支，再revert dev分支。至此，我们的初始环境已经搭建完成，后续的测试都是基于以上环境进行的。



## 2. 情况一： dev分支无新提交，在master分支 重新merge dev分支

```shell
$ git merge --no-ff dev		# 试图重新合并dev分支
Already up-to-date.
```

我们试着重新合并dev分支，结果却发现提示`Already up-to-date.`。为什么会这样呢？原因是：在第一次合并dev分支并进行revert dev分支的操作后，git认为你已经抛弃了dev分支的操作，git认为两个分支的最近公共交点是`f60e686`；此时再重新merge dev（且dev分支没有新的提交），git认为dev分支已经在master上了，所以提示`Already up-to-date.`。

如果想达到重新merge dev分支的效果，那又该如何操作呢？先来看看，这样做最终的目的是什么？很简单：dev分支没有任何提交，重新merge dev，意味着回到`3463ae0`状态。所以我们直接reset到 merge节点即可：

```shell
$ git reset --hard 3463ae0			# reset
HEAD is now at 3463ae0 Merge branch 'dev'

$ git lg
*   3463ae0 - (HEAD -> master) Merge branch 'dev' (25 minutes ago) <htw112896>
|\
| * 99cd6db - (dev) add dev_2 (26 minutes ago) <htw112896>
| * 1f73f04 - add dev_1 (26 minutes ago) <htw112896>
|/
* 074af34 - add master_1 (27 minutes ago) <htw112896>
```



## 3. 情况二：在dev分支 merge master分支

```shell
$ git checkout dev			# 切换到dev分支
Switched to branch 'dev'

$ git merge --no-ff master	# 合并master分支
Removing dev_2
Removing dev_1
Merge made by the 'recursive' strategy.
 dev_1 | 0
 dev_2 | 0
 2 files changed, 0 insertions(+), 0 deletions(-)
 delete mode 100644 dev_1
 delete mode 100644 dev_2
 
$ git lg
*   b089426 - (HEAD -> dev) Merge branch 'master' into dev (43 seconds ago) <htw112896>
|\
| * f60e686 - (master) Revert "Merge branch 'dev'" (22 minutes ago) <htw112896>
| *   3463ae0 - Merge branch 'dev' (27 minutes ago) <htw112896>
| |\
| |/
|/|
* | 99cd6db - add dev_2 (28 minutes ago) <htw112896>
* | 1f73f04 - add dev_1 (28 minutes ago) <htw112896>
|/
* 074af34 - add master_1 (29 minutes ago) <htw112896>
```

可以发现，合并master分支后，实际上就是将dev HEAD指针直接指到master HEAD处。也就是将revert操作在dev分支上再执行一遍。这也意味着，dev分支放弃了之前的所有提交操作。



## 4. 情况三： dev分支有新提交，然后master分支合并 dev分支

```shell
$ git checkout dev			# 切换到dev分支
Switched to branch 'dev'

$ touch dev_3

$ git add .

$ git commit -m 'add dev_3'		# dev新提交
[dev 5fe195f] add dev_3
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 dev_3

$ git lg
* 5fe195f - (HEAD -> dev) add dev_3 (2 seconds ago) <htw112896>
* 99cd6db - add dev_2 (34 minutes ago) <htw112896>
* 1f73f04 - add dev_1 (34 minutes ago) <htw112896>
* 074af34 - add master_1 (35 minutes ago) <htw112896>

$ git checkout master	# 切换到master分支
Switched to branch 'master'

$ git merge --no-ff dev			# master重新merge dev分支
Merge made by the 'recursive' strategy.
 dev_3 | 0
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 dev_3

$ git lg
*   7c61c25 - (HEAD -> master) Merge branch 'dev' (4 seconds ago) <htw112896>
|\
| * 5fe195f - (dev) add dev_3 (21 seconds ago) <htw112896>
* | f60e686 - Revert "Merge branch 'dev'" (28 minutes ago) <htw112896>
* |   3463ae0 - Merge branch 'dev' (34 minutes ago) <htw112896>
|\ \
| |/
| * 99cd6db - add dev_2 (35 minutes ago) <htw112896>
| * 1f73f04 - add dev_1 (35 minutes ago) <htw112896>
|/
* 074af34 - add master_1 (36 minutes ago) <htw112896>

$ ls		# 要特别注意此时本地目录所包含的文件
dev_3    master_1
```

我们发现master分支从dev分支上获取的只有`dev_3`（也就是dev分支新提交的内容），之前dev分支的commit `add dev_1`和`add dev_2`所生成的文件并没到重新merge过来！正常思路的话，我们认为master 分支虽然revert了dev分支的操作，但是现在重新merge了dev，那么dev分支上的所有提交应该都重新merge到master。难道是git有bug？

这一所谓的bug其实是没理解git的原理所导致误解。为什么文件dev_1和dev_2不存在呢？道理在**情况一**中已经说明了。master上进行revert dev操作后（f60e686），git认为之前dev上的操作被废弃了。之后dev分支重新产生commit后，再merge到master。此时，git认为只产生了一次新的commit，之前被revert的操作不会重新执行。

或者简单理解，git认为此时的dev分支和master分支最近的共同交点是`f60e686`，所以merge dev只会把dev分支`99cd6db`之后的操作进行merge。

