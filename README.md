# NASDAQ-ITCH-5.0-Parser
Parses and prints the NASDAQ ITCH 5.0 data

Thanks to Quannabe. I just modified the YAML file for the nerw ITCH 5.0 format.


#How To

 The main function is location in ```src/parse.java```. Just enter the file name and path to the ITCH file that you wanna parse here [File Name](https://github.com/Amay22/NASDAQ-ITCH-5.0-Parser/blob/master/src/Parse.java#L56).
 
## Run Code

Compile the code and run it. Navigate to the src directory and run the following commands in your terminal. 

```
  javac Parse.java
  java Parse [ITCH file path]
```

#Data

Download raw ITCH 5.0 data from the following link:

ftp://emi.nasdaq.com/ITCH/08022014.NASDAQ_ITCH50.gz

#DATA FORMAT

http://www.nasdaqtrader.com/content/technicalsupport/specifications/dataproducts/NQTVITCHspecification.pdf

(For Nasdaq Genium)

http://business.nasdaq.com/Docs/ITCHRefDataGuideNFXv2_00_tcm5044-18017.pdf
