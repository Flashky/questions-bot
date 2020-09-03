# questions-bot

## Program arguments

Temporary, while starting the development, the oauth token can be added via the first program argument with no "oauth:" prefix on it.

## Configuration properties to set

You can set this configuration properties by overriding either ``application.yml`` or as environment variables.

OAuth configuration:

- twitch.oauth.client-id
- twitch.oauth.client-secret

IRC configuration:

- twitch.irc.host: default value if not set irc.chat.twitch.tv
- twitch.irc.nick: Bot nick
- twitch.irc.channel: Channel to join without hashtag (#)

