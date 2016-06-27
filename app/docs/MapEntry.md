
# MapEntry

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**mapId** | [**UUID**](UUID.md) | Id of the Map entity this entry belongs to | 
**relativeX** | [**BigDecimal**](BigDecimal.md) | Relative X position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteX &#x3D; [RelativeX / 100] * [Map.Image.Width] | 
**relativeY** | [**BigDecimal**](BigDecimal.md) | Relative Y position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteY &#x3D; [RelativeY / 100] * [Map.Image.Height] | 
**relativeTapRadius** | [**BigDecimal**](BigDecimal.md) | Radius around the center defined by x/y which should be tap-active for events regarding this map entry. Scale 0-1, relative to Height. To convert to absolute radius, use this formula: AbsoluteTapRadius &#x3D; RelativeTapradius * [Map.Image.Height] | 
**markerType** | **String** | Type of marker. Valid options: &#39;Dealer&#39; | 
**targetId** | [**UUID**](UUID.md) | Target entity referenced by the marker, e.G. for a marker of type &#39;Dealer&#39;, this would id of the dealer. |  [optional]
**targetDescription** | **String** | Alternative description for marker types that do not reference an entity (or need additional data9, not used yet. |  [optional]



