# Bob Teaches Tech Android App

This is a lightweight Android app that loads:

- The latest posts from **bobteachestech.com** (RSS feed).
- The latest videos from **@bobteachestech** on YouTube (Atom feed).

## What it does

- Displays two tabs: **Posts** and **Videos**.
- Fetches the RSS/Atom feeds on launch and provides a retry button on failure.
- Tapping a card opens the article or video in the browser/YouTube app.

## Configure feeds

The feed URLs live in `FeedRepository`:

```kotlin
const val POSTS_FEED_URL = "https://bobteachestech.com/feed/"
const val YOUTUBE_FEED_URL = "https://www.youtube.com/feeds/videos.xml?user=bobteachestech"
```

If YouTube changes the handle feed, replace the `YOUTUBE_FEED_URL` with the channel-id feed URL.

## Build an APK (easy way)

1. Ensure Android Studio or a JDK 17 install is available.
2. Run:

```bash
./gradlew assembleDebug
```

Your debug APK will be generated at:

```
app/build/outputs/apk/debug/app-debug.apk
```

You can share that APK directly with anyone who needs to install it.
