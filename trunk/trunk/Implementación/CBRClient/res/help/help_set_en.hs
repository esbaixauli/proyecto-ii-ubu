<?xml version="1.0" encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">
 

<helpset version="1.0">
        <title>CBR help</title>
        <maps>
                <!-- Pagina por defecto al mostrar la ayuda -->
                <homeID>MainFrame</homeID>
                <!-- Que mapa deseamos -->
                <mapref location="map_file_en.jhm"/>
        </maps>

  <view>
     <name>Content Table</name>
     <label>Content table</label>
     <type>javax.help.TOCView</type>
     <data>toc_en.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>indice_en.xml</data>
  </view>
  
  <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>
  
</helpset>

