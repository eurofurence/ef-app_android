
# LinkFragment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**fragmentType** | [**FragmentTypeEnum**](#FragmentTypeEnum) |  | 
**name** | **String** |  |  [optional]
**target** | **String** | * For FragmentType &#x60;DealerDetail&#x60;: The &#x60;Id&#x60; of the dealer record the link is referencing to.  * For FragmentType &#x60;MapEntry&#x60;: The &#x60;Id&#x60; of the map entry record the link is referencing to.  * For FragmentType &#x60;EventConferenceRoom&#x60;: The &#x60;Id&#x60; of the event conference room record the link is referencing to.  * For FragmentType &#x60;MapExternal&#x60;: An stringified json object.    * Acceptable properties and their expected value (type):      * &#x60;name&#x60; - name of target POI (*string*)      * &#x60;street&#x60; - street name (*string*)      * &#x60;house&#x60; - house humber (*string*)      * &#x60;zip&#x60; - zip code of city (*string*)      * &#x60;city&#x60; - city (*string*)      * &#x60;country&#x60; - country (*string*)      * &#x60;country-a3&#x60; - ISO 3166-1 alpha-3 code for country [http://unstats.un.org/unsd/methods/m49/m49alpha.htm] (*string*)      * &#x60;lat&#x60; - latitude (*decimal*)      * &#x60;lon&#x60; - longitude (*decimal*)    * Example:      * &#x60;{ name: \&quot;Estrel Hotel Berlin\&quot;, house: \&quot;225\&quot;, street: \&quot;Sonnenallee\&quot;, zip: \&quot;12057\&quot;, city: \&quot;Berlin\&quot;, country: \&quot;Germany\&quot;, lat: 52.473336, lon: 13.458729 }&#x60; | 


<a name="FragmentTypeEnum"></a>
## Enum: FragmentTypeEnum
Name | Value
---- | -----



