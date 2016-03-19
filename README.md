# AndroidKit
我的android studio项目
## 【1】快速使用
### gradle添加
compile 'com.snicesoft:androidkit:1.6.0'
### Application
```java
public class KitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogKit.customTagPrefix = "Kit_";
        //======初始化Bitmap组件======

        //bitmap组件之xutils_bitmap
        XUtilsBitmapKit.getInstance(getApplicationContext());
        //bitmap组件之VolleyImageLoader
        //VolleyImageLoader.init(getApplicationContext());
        //bitmap组件之universal-image-loader
        //UILBitmapKit.getInstance(getApplicationContext());

        //======初始化Http组件======

        //1:http组件之volley
        VolleyHttpKit.getInstance(getApplicationContext());
        //2:http组件之Okhttp
        //OkhttpKit.getInstance();

        //======初始化API======
        API.getInstance().initTest(Config.Scheme.HTTPS, "192.168.0.122", 0, "userinfo/");
        API.getInstance().initProduct(Config.Scheme.HTTPS, "89.23.78.345", 0, "userinfo/");
        API.getInstance().init(ConfigFactory.Mode.TEST);
    }
}
```
### net网络请求数据package
#### ....net.api
API.java
```java
public final class APIADDRESS {
    public final static class User {
        public final static String USER = API.config.baseUrl()+"mobile/user";
        public final static String USER_LOGIN =  API.config.baseUrl()+"mobile/user/login";
    }
}
```
#### ....net.data
存放接口数据模型
#### ....net.controller
接口请求Controller
## 【2】包含
### 网络请求
可以兼容Volley、OkHttp等网络请求库
### 图片加载
可以兼容xUtils、ImageLoder等图片加载库
## 【3】开源协议
```
 Copyright (C) 2015, zhuzhe
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ```
