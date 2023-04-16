# TaoBaoUnion
领券联盟
- 4.14
    
    添加依赖，页面设计，绑定控件，设定监听
    
- 4.15
    - 编写首页布局
        
        分析页面结构，确定使用控件，为控件编写样式，设置shape样式
        
    - 页面交互
        1. 写回调接口，让fragment实现回调接口
            1. 比如fragment的数据来源于网络
            2. 那么获取数据的过程肯定不能在UI类中写
            3. 所以要在presenter中获取数据
        2. 写presenter接口，实现presenter
            1. presenter获取数据之后，调用UI中的回调方法更新UI
            2. 更新UI的方法是由UI实现的，但是由presenter调用的
            3. fragment中需要被回调的方法写在接口中
            4. 将接口作为registerCallback()方法的参数传入presenter
        3. presenter中注册回调接口
        4. presenter中调用回调接口中的方法
        5. 通过retrofit获取网络数据
            1. 创建RetrofitManager单例类
            2. 向外界提供Retrofit对象
            3. 编写Api接口，提供网络连接方法
            4. 在presenter中获取retrofit对象，调用create方法获取Api对象
            5. 调用Api对象的方法，获得Call对象 task
            6. task.enqueue() 处理结果
    - TabLayout和viewPager
        
        TabLayout是导航栏
        
        viewPager需要设置适配器
        
    - 不同数据请求结果展示不同页面
