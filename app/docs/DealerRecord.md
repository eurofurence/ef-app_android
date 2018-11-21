
# DealerRecord

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**lastChangeDateTimeUtc** | [**Date**](Date.md) |  | 
**id** | [**UUID**](UUID.md) |  | 
**registrationNumber** | **Integer** | Registration number (as on badge) of the attendee that acts on behalf/represents this dealer. | 
**attendeeNickname** | **String** | Nickname number (as on badge) of the attendee that acts on behalf/represents this dealer. | 
**displayName** | **String** | **(pba)** Name under which this dealer is acting, e.G. name of the company or brand. | 
**merchandise** | **String** | **(pba)** Brief description of merchandise/services offered. | 
**shortDescription** | **String** | **(pba)** Short description/personal introduction about the dealer. |  [optional]
**aboutTheArtistText** | **String** | **(pba)** Variable length, bio of the artist/dealer. |  [optional]
**aboutTheArtText** | **String** | **(pba)** Variable length, description of the art/goods/services sold. |  [optional]
**links** | [**List&lt;LinkFragment&gt;**](LinkFragment.md) | **(pba)** Link fragments to external website(s) of the dealer. | 
**twitterHandle** | **String** | **(pba)** Twitter handle of the dealer. |  [optional]
**telegramHandle** | **String** | **(pba)** Telegram handle of the dealer. |  [optional]
**attendsOnThursday** | **Boolean** | Flag indicating whether the dealer is present at the dealers den on thursday. |  [optional]
**attendsOnFriday** | **Boolean** | Flag indicating whether the dealer is present at the dealers den on friday. |  [optional]
**attendsOnSaturday** | **Boolean** | Flag indicating whether the dealer is present at the dealers den on saturday. |  [optional]
**artPreviewCaption** | **String** | **(pba)** Variable length, caption/subtext that describes the &#39;art preview&#39; image. |  [optional]
**artistThumbnailImageId** | [**UUID**](UUID.md) | **(pba)** ImageId of the thumbnail image (square) that represents the dealer.  Used whenever multiple dealers are listed or a small, squared icon is needed. |  [optional]
**artistImageId** | [**UUID**](UUID.md) | **(pba)** ImageId of the artist image (any aspect ratio) that represents the dealer.  Usually a personal photo / logo / badge, or a high-res version of the thumbnail image. |  [optional]
**artPreviewImageId** | [**UUID**](UUID.md) | **(pba)** ImageId of an art/merchandise sample sold/offered by the dealer. |  [optional]
**isAfterDark** | **Boolean** | Flag indicating whether the dealer is located at the after dark dealers den. |  [optional]
**categories** | **List&lt;String&gt;** | **(pba)** List of standardized categories that apply to the goods/services sold/offered by the dealer. |  [optional]



