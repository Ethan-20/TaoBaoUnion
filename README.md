# TaoBaoUnion
领券联盟
这款软件是一款领券APP，基于MVP架构开发。界面设计美观，布局合理，包含底部导航栏、RecyclerView、TabLayout、NestedScrollView等控件，并解决了嵌套时的滑动冲突问题和优化了滑动卡顿现象。同时，软件实现了轮播图以及轮播图与导航栏的联动。使用Retrofit连接淘宝客API返回商品数据，并能够处理网络请求的多种状态，确保用户在不同的网络环境下都能正常使用。图片加载技术采用Glide，让图片加载速度更快，同时保证图片质量。刷新功能则采用twinklingRefreshLayout，用户可以通过下拉刷新获得最新的商品信息。

主要工作：
1. 界面布局编写（包括自定义控件）
2. 解决布局嵌套时的滑动冲突问题
3. 优化滑动时的卡顿现象
4. 实现轮播图与导航栏的联动
5. 使用Retrofit连接淘宝客API，处理网络请求的多种状态
6. 实现刷新功能，包括下拉刷新和上拉加载更多
