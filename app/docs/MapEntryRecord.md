
# MapEntryRecord

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | [**UUID**](UUID.md) |  | 
**relativeX** | **Double** | \&quot;X\&quot; coordinate of the *center* of a *circular area*, expressed as &#x60;[Relative Fraction of Map Image Width * 100]&#x60;.    *  A value of &#x60;RelativeX&#x3D;50&#x60; indicates it&#39;s on the horizontal middle of a map, and results in an absolute X of 1000 on a map that is 2000 pixels wide,       or 500 on a map that is 1000 pixels wide. 0 would indicate the far left side, where as 100 indicates the far right side of the image. |  [optional]
**relativeY** | **Double** | \&quot;Y\&quot; coordinate of the *center* of a *circular area*, expressed as &#x60;[Relative Fraction of Map Image Height * 100]&#x60;.   *  A value of &#x60;RelativeY&#x3D;50&#x60; indicates it&#39;s on the vertical middle of a map, and results in an absolute Y of 1000 on a map that is 2000 pixels in height,      or 500 on a map that is 1000 in height. 0 would indicate the top side, where as 100 indicates the bottom side of the image. |  [optional]
**relativeTapRadius** | **Double** | \&quot;Radius\&quot; of a *circular area* (the center of which described with RelativeX and RelativeY), expressed as &#x60;[Relative Fraction of Map Image Height]&#x60;.   *  A value of &#x60;RelativeTapRadius&#x3D;0.02&#x60; indicates that the circle has an absolute tap radius of 20 pixels (and a diameter of 40 pixels) on a map that is       1000 pixels in height, or a tap radius of 10 pixels (and a diameter of 20 pixels) on a map that is 500 pixels in height. |  [optional]
**link** | [**LinkFragment**](LinkFragment.md) |  |  [optional]



