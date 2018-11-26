# LibApplication
Libs for myself


#### 1、rxbus
rxJava + eventBus

  ## usage:
    # 1、define a event class
    public final static Observable<XXXEvent> observable = RxBus.getInstance().register(XXXEvent.class, XXXEvent.class);
    
    # 2、post a particular event with or without a constructor
    RxBus.getInstance().post(XXXEvent.class, new XXXEvent());
    
    # 3、subscribe the observable
    XXXEvent.observable.subscribe();


#### 2、recycleviewclick
onClickListener of recycleViewItem, both onItemClick and onLongClick

  ## usage:
    recycleView.addOnItemTouchListener(new RecyclerViewClickListener(this, recycleView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO sigleTap event
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //TODO longClick event
            }
        }));


#### 3、update
auto update tips and action to download with app url from browser

  ## usage:
    # 1、create a config json which can be touched from Internet such as
    {
      "latestVersion": "x.x.x",
      "latestVersionCode": x,
      "url": "http://xxx.xxx.xxx.xxx:xxxx/xxxx.apk", //url to download the app
      "releaseNotes": [
        "1、AndroidX支持",
        "2、bugs fix"
      ]
    }
    
    # 2、invoke the util class
    UpdateUtil.getUpdateDetail(this, url); //json file url from step 1
      

#### 4、flickview
view flicks with any view

  ## usage:
    # 1、startFlick, fromAlpha: original alpha, recommend value '1'; toAlpha: final alpha, recommend value '0'; durationMill: alphaAnimation time, recommend value '800'
    FlickHelper.getFlickHelper().startFlick(view, fromAlpha, 0, 800);
  
    # 2、stopFlick
    FlickHelper.getFlickHelper()stopFlick()
