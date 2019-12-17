### 背景
移动开发的同学都知道，打包前的检查是很重要的，不管你是手动打包，还是提交代码Jenkins打包，代码层面的检查都是跑不掉的，开发时写的一些变量、环境，或许是测试环境的，提交代码或者打包时，，万一没改过来就GG了。

吃过两次亏了，打成了测试环境的包，幸好发现及时，没有酿成大错，但也是后怕了，所以就撸了个插件，用来打包或者提交代码前前检查代码中的环境等配置是否是正确的...

比方说：我在项目中定义了好几个变量，来区分现在是否是测试环境、是否打印日志、是否更改价格为测试价格等，打包的时候，这些肯定是要换成正式的值，需要逐一去排查。
```
    private boolean isTestServer = false;
    private boolean isOpenAdLog = true;
    public static boolean isDebug = false;
```

### 插件使用
#### 导入插件
方法一：插件管理搜索：Release Check，安装就可以啦  
方法二：本地安装，导入zip包安装，包地址见：

#### 使用
1、首先要找到插件
![TIM截图20191217123409.png](https://upload-images.jianshu.io/upload_images/3650692-0b946269a18293e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2、输入各项检查项  
输入类名、变量名以及变量的正确值，可以添加多项检查项
![TIM截图20191217123610.png](https://upload-images.jianshu.io/upload_images/3650692-bf95cf2a7d8bf324.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
3、打包/提交代码前检查  
**白色为通过，当前值和所设置检查项一致，红色的话表示不一致，需要去确认下是否需要修改**
![TIM截图20191217123628.png](https://upload-images.jianshu.io/upload_images/3650692-fb3b5a3b8031b33f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 最后
不管怎么说，还是要写代码和打包的时候自个儿注意点，别搞乱了环境，插件只是辅助emmmmm。


