# 重命名并分类喜马拉雅声音uuid.x2m

### 介绍
将喜马拉雅app(安卓)中获取的uuid.x2m声音文件进行重命名

例如:
将2f5b2133c45f7b0971567d40f2fdb60a.x2m
转换为:0056 历史 《隋唐制度渊源略论稿》：陈寅恪揭示隋唐强盛的历史渊源.m4a

注意,您必须要获取ximalaya.db文件!(sqlite数据库,可以用相关应用打开)

获取该文件手机必须进行root,否则无法访问该文件所在的文件夹

如果您已经root,那么在app中下载声音后,直接搜索并导出ximalaya.db

如果您没有符合条件的设备,可以使用虚拟机/安卓模拟器

主要是喜马拉雅他vip声音会过期,这就很过分了...

再,再说了...

**书是人类进步的阶梯**

**读书人的事,怎么能叫偷呢!**


### 使用方法
基于maven创建的本工程,主要依赖了```sqlite-jdbc```和```junit```

主程序在```src/test/java/com.example.demo/RunMe```中

1.在RunMe中,设置数据库(ximalaya.db,需要从已root的手机中获取),源文件位置(m4aPath),需要储存的位置(saveTo)

2.需要将m2x转换为m4a的软件,转换工具位置/resources/tools/ximalaya_downloader_&_x2m_decoder.rar
只需要使用x2m to m4a功能.也可以网上自行查找同类工具.

3.运行```main()```即可(本人比较喜欢使用@Test来执行)

### 运行原理
ximalaya.db中有储存文件大小,通过比对大小来区分声音,因此可能出现存在多个结果而无法区分

那只能人工分一下呗,我测试时单次样本≈1.4k,最后重复的结果只有20个

因此每次比对样本少点不太会出现多结果

你问为什么不用sequenceid来区分?

我做测试的时候uuid貌似加盐/加密了,无法以此进行区分

### 其他备注
如果有重复的结果,程序不会进行移动(已改名的文件会移动至指定文件夹下)

可以执行2次本程序,然后复制终端中未分类成功的文件的尺寸

然后使用例如以下sql语句获取结果集,进行人工比对
```sql
select * from newtrack where
     downloadedsize = 5932013
  or downloadedsize = 2578116
  or downloadedsize = 13555709
  or downloadedsize = 13971048
  or downloadedsize = 14700023
  or downloadedsize = 14755656
  or downloadedsize = 5932013
  or downloadedsize = 10316804
  or downloadedsize = 15637189
  or downloadedsize = 2578116
  or downloadedsize = 10072077
  or downloadedsize = 13555709
  or downloadedsize = 10072077
  or downloadedsize = 10316804
  or downloadedsize = 14755656
  or downloadedsize = 12600875
  or downloadedsize = 14700023
  or downloadedsize = 12600875;
```

  
