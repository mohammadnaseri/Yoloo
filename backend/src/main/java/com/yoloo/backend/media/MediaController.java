package com.yoloo.backend.media;

import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.users.User;
import com.google.common.base.Optional;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.yoloo.backend.account.Account;
import com.yoloo.backend.base.Controller;
import com.yoloo.backend.util.CollectionTransformer;
import io.reactivex.Observable;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;

import static com.yoloo.backend.OfyService.ofy;

@AllArgsConstructor(staticName = "create")
public class MediaController extends Controller {

  private static final Logger logger = Logger.getLogger(MediaController.class.getName());

  /**
   * Maximum number of questions to return.
   */
  private static final int DEFAULT_LIST_LIMIT = 20;

  private MediaService mediaService;

  /**
   * Gets media.
   *
   * @param mediaId the media id
   * @param user the user
   * @return the media
   */
  public MediaEntity getMedia(@Nonnull String mediaId, User user) {

    final Key<MediaEntity> mediaKey = Key.create(mediaId);

    return ofy().load().key(mediaKey).now();
  }

  /**
   * List collection response.
   *
   * @param userId the webssafe account id
   * @param limit the limit
   * @param cursor the cursor
   * @param user the user
   * @return the collection response
   */
  public CollectionResponse<MediaEntity> listMedias(String userId, Optional<Integer> limit,
      Optional<String> cursor, User user) {

    // Create account key from websafe id.
    final Key<Account> authKey = Key.create(userId);

    Query<MediaEntity> query =
        ofy().load().type(MediaEntity.class).ancestor(authKey).order("-" + MediaEntity.FIELD_CREATED);

    // Fetch items from beginning from cursor.
    query = cursor.isPresent() ? query.startAt(Cursor.fromWebSafeString(cursor.get())) : query;

    query = query.limit(limit.or(DEFAULT_LIST_LIMIT));

    final QueryResultIterator<MediaEntity> qi = query.iterator();

    return Observable
        .just(qi)
        .filter(Iterator::hasNext)
        .map(Iterator::next)
        .toList(DEFAULT_LIST_LIMIT)
        .toObservable()
        .compose(CollectionTransformer.create(qi.getCursor().toWebSafeString()))
        .blockingSingle();
  }

  public CollectionResponse<MediaEntity> listRecentMedias(Optional<Integer> limit,
      Optional<String> cursor) {

    Query<MediaEntity> query = ofy().load().type(MediaEntity.class).order("-" + MediaEntity.FIELD_CREATED);

    // Fetch items from beginning from cursor.
    query = cursor.isPresent() ? query.startAt(Cursor.fromWebSafeString(cursor.get())) : query;

    query = query.limit(limit.or(DEFAULT_LIST_LIMIT));

    final QueryResultIterator<MediaEntity> qi = query.iterator();

    return Observable
        .just(qi)
        .filter(Iterator::hasNext)
        .map(Iterator::next)
        .toList(DEFAULT_LIST_LIMIT)
        .toObservable()
        .compose(CollectionTransformer.create(qi.getCursor().toWebSafeString()))
        .blockingSingle();
  }
}
