# Dicts
Android app that shows results from several search providers.

*Check out the [server repo](https://github.com/eviabs/web-dicts) for more information.*


<img src="https://user-images.githubusercontent.com/14614396/40269388-80cbf31c-5b86-11e8-8aa8-88783a822f29.png" width="30%" height="30%" />


## Demo (gifs might take some time to load)
<img src="https://user-images.githubusercontent.com/14614396/40269338-fa4ca7b4-5b85-11e8-922c-c3a640635975.gif" width="184" height="325" />   <img src="https://user-images.githubusercontent.com/14614396/40269339-fa7876c8-5b85-11e8-9c24-0b5d329eb37e.gif" width="184" height="325" />   <img src="https://user-images.githubusercontent.com/14614396/40269340-fa9f340c-5b85-11e8-80cb-452ffeb36ba4.gif" width="184" height="325" />


## Download
App is available at play store, but you are welcome to download the source code and use it as you please.

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="30%" height="30%">](https://play.google.com/store/apps/details?id=com.eviabs.dicts)

## Modify the Dictionaries (Search Providers)
The app comes with some default dictionaries (which are called Search Providers).
If you wish to add your own search providers or remove the default ones, there are several steps to achieve that.

### Add a Dictionary 
We will now demonstrate how to add a new search provider to the app. For this practice, we will add Wikipedia.
*Note that there are few superclasses that you will extend in order to add a provider. Those superclasses will take care of most of the app's functionality (e.g. showing "no results" card when no results are available. or showing "server error" card with a retry button when an error occurs). You could override any of the superclass' methods if you with to achieve more complex behavior. In such case, the code is documented and you are advised to go throgh it.*

1) First, make sure that your server (see [server repo](https://github.com/eviabs/web-dicts)) supports your search provider (see [server repo](https://github.com/eviabs/web-dicts/blob/master/README.md#add-seach-provider))

2) Go to `ApiConsts` class under `ApiClients` package, and add the following String: 

   ```java 
      public static final String DICTIONARY_WIKIPEDIA = "Wikipedia";
   ```
   
   The String holds the display name of the search provider.
   
   And then add that String to the array `SEARCH_PROVIDERS`:
   ```java
      public static final String[] SEARCH_PROVIDERS = {
              DICTIONARY_WIKIPEDIA
      };
    ```
    
    This array defines what search providers are available.

3) Go to `ClientFactory` class under `ApiClients` package, and add the following code:

   ```java 
      interface Client {
          @GET("/dic/wikipedia")
          Call<ResponseBody> getWikipediaDefinitions(@Query("t") String term);
      }
    ```
    This code defines a [Retrofit](http://square.github.io/retrofit/) client.
    
    Now, we will add that call to our Client factory:
   ```java 
    public static Call<ResponseBody> getCall(Bundle queryBundle, String searchProvider, String url) {
      switch (searchProvider) {
        case ApiConsts.DICTIONARY_WIKIPEDIA:
          return getClient(url).getWikipediaDefinitions(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM));
          // ...rest of calls and default call...
      }
    }
    ```
     
4) Now, we will create Java objects that wraps our json response from the server.
   
   You can use [jsonschema2pojo](http://www.jsonschema2pojo.org/) to generate the appropriate java objects.
   
   Place the objects under `SearchProviders` package. 
   
   This objects extends a base class called `Results`:
   
   ```java 
      public class WikipediaResults extends Results {
          @SerializedName("title")
          @Expose
          private String title;

          @SerializedName("extract")
          @Expose
          private String extract;

          public WikipediaResults(int error, String title, String extract) {
              super(error);
              this.title = title;
              this.extract = extract;
          }

          public String getTitle() {
              return title;
          }

          public void setTitle(String title) {
              this.title = title;
          }

          public String getExtract() {
              return extract;
          }

          public void setExtract(String extract) {
              this.extract = extract;
          }

          @Override
          public int getCount() {
              return (getTitle().equals("")) ? 0 : 1;
          }
      }
    ```
    There are 2 important things to notice:
    * On the constructor `WikipediaResults`, the parameter `error` must be set by the super class constructor: `super(error)`.
    * You must override the method `getCount()` which tells to the objects how naby results are available. In our case, there is a single result if the title is not empty.

5) Now, we will create a layout for our data. This layout will be shoen inside the results cards.
   Under `res/layout` create an xml layout file called `definition_wikipedia.xml`:

   ```xml 
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        xmlns:tools="http://schemas.android.com/tools"
        android:padding="25dp"
        android:orientation="vertical">

            <android.support.v7.widget.AppCompatTextView
                style="@android:style/TextAppearance.Holo.Large"
                android:id="@+id/textViewWikipediaTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textStyle="bold"
                android:textSize="30sp"
                android:textIsSelectable="false"
                android:layout_marginEnd="@dimen/dictionary_icon_size"
                tools:text="title" />

        <android.support.v7.widget.AppCompatTextView
            style="@android:style/TextAppearance.Holo.Large"
            android:id="@+id/textViewWikipediaExtract"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textSize="20sp"
            android:textIsSelectable="false"
            android:layout_marginEnd="@dimen/dictionary_icon_size"
            tools:text="extract" />

    </LinearLayout>
    ```
    This layout consist of 2 `TextView` which hold the title and the extract (defenition)  of the search result. It looks like that:
    ![wiki_layout](https://user-images.githubusercontent.com/14614396/42425133-ef2f899c-8320-11e8-8088-9cb197c1fcec.png)

    
6) Now, we will create an adapter under `Adapters` package that is called `WikipediaTermAdapter`.
   This adapter is a subclass of `TermAdapter`. The superclass will take care of most of the hard work.
   
   ```java
    public class WikipediaTermAdapter extends TermAdapter {

        public class MyViewHolder extends TermAdapterViewHolder {
            public TextView title;
            public TextView extract;


            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.textViewWikipediaTitle);
                extract = (TextView) view.findViewById(R.id.textViewWikipediaExtract);

            }
        }
        public WikipediaTermAdapter(Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
            super(mContext, error, responseBody, retryOnClickListener);
        }

        @Override
        public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
            innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
            return innerTermViewHolder;

        }

        @Override
        public void setDefinitionLayout(InnerTermViewHolder oldHolder, int position) {

            WikipediaResults term = ((WikipediaResults) results);
            MyViewHolder holder = ((MyViewHolder)oldHolder.outerTermAdapter);

            holder.title.setText(term.getTitle());
            holder.extract.setText(term.getExtract());
        }

        @Override
        protected Results createResultsObject(ResponseBody responseBody) {
            if (responseBody != null) {
                try {
                    return new Gson().fromJson(responseBody.string(), WikipediaResults.class);
                } catch (IOException ex) {
                    // do nothing
                }
            }
            return null;
        }

        @Override
        protected int getDefinitionLayoutId() {
            return R.layout.definition_wikipedia;
        }

        @Override
        protected Drawable getIconDrawable() {
            return mContext.getResources().getDrawable(R.drawable.wikipedia_480);
        }
    }

    ```
    
    Let's take a look at the important things in here:
    * If you've already used [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) before, you may see that the `MyViewHolder` class is a standard `ViewHolder` that is used to connect to the layout we have just created.
    * Create a `WikipediaTermAdapter` constructor that calls its super class constructor.
    * Override the method `InnerTermViewHolder` as shown. This way, we make sure that the super class gets the correct `MyViewHolder` class.
    * Implement the abstract method `setDefinitionLayout`. This method sets the layput as you wish. Not that we take the method's parameters and downcast them to the classes we have just created. It's your responsibility to make sure that you make the correct cast.
    * Implement the abstract method `createResultsObject` which uses [Gson](https://github.com/google/gson) to convert our json response to our java objects.
    * Implement the abstract method `getDefinitionLayoutId` which return the appropriate layout id.
    * If you wish to add an icon to your card result, override the method `getIconDrawable`. If you won't not icon will be shown.
        

7) Go to `TermAdapterFactory` class under `Adapters` package, and add the adapter we have just created to the factory:

   ```java 
    public static TermAdapter getTermAdapter(String searchProviderName, Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        switch (searchProviderName) {
            case ApiConsts.DICTIONARY_WIKIPEDIA:
                return new WikipediaTermAdapter(mContext, error, responseBody, retryOnClickListener);
        }

        return null;
    }
    ```
    And that's it.

### Remove Dictionaries
To remove search providers Go to `ApiConsts` class under `ApiClients` package, and remove the unwanted providers from the array `SEARCH_PROVIDERS`:
```java
      public static final String[] SEARCH_PROVIDERS = {
           DICTIONARY_IMAGES,
           // DICTIONARY_MORIFX, <- Remove this line
           // DICTIONARY_MILOG, <- Remove this line
           DICTIONARY_WIKIPEDIA,
           DICTIONARY_URBAN_DICTIONARY
      };
 ```
 Now olny images, wikipedia and urban dictionary are available throughout the app.
         
## Authors

**Evyatar Ben-Shitrit** - [eviabs](https://github.com/eviabs)

## License

This project is licensed under the MIT License.
