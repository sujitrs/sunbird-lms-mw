package org.sunbird.feed;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.sunbird.bean.ShadowUser;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.feed.impl.FeedServiceImpl;
import org.sunbird.models.user.Feed;

/**
 * this class will be used as a Util for inserting Feed in table
 *
 * @author amit
 */
public class FeedUtil {
  private static IFeedService feedService = FeedServiceImpl.getInstance();
  private static final String ORG_MIGRATION_ACTION = "OrgMigrationAction";
  public static final String UNREAD = "unread";
  public static final String READ = "read";

  public static void saveFeed(ShadowUser shadowUser, List<String> userIds) {
    Map<String, Object> reqMap = new HashMap<>();
    reqMap.put(JsonKey.USER_ID, userIds.get(0));
    reqMap.put(JsonKey.CATEGORY, ORG_MIGRATION_ACTION);
    List<Feed> feedList = feedService.getRecordsByProperties(reqMap);
    Optional<Feed> optional =
        feedList
            .stream()
            .filter(
                feed -> StringUtils.endsWithIgnoreCase(feed.getCategory(), ORG_MIGRATION_ACTION))
            .findFirst();
    Feed feed = optional.get();
    if (null == feed) {
      feedService.insert(getFeedObj(shadowUser, userIds));
    } else {
      Map<String, Object> data = feed.getData();
      List<String> channelList = (List<String>) data.get(JsonKey.PROSPECT_CHANNELS);
      channelList.add(shadowUser.getChannel());
      feedService.update(feed);
    }
  }

  private static Feed getFeedObj(ShadowUser shadowUser, List<String> userIds) {
    Feed feed = new Feed();
    feed.setPriority(1);
    feed.setCreatedBy(shadowUser.getAddedBy());
    feed.setStatus(UNREAD);
    feed.setCategory(ORG_MIGRATION_ACTION);
    Map<String, Object> prospectsChannel = new HashMap<>();
    List<String> channelList = new ArrayList<>();
    channelList.add(shadowUser.getChannel());
    prospectsChannel.put(JsonKey.PROSPECT_CHANNELS, channelList);
    feed.setData(prospectsChannel);
    feed.setUserId(userIds.get(0));
    return feed;
  }
}
