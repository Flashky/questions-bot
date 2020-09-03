# questions-bot

## Program arguments

Temporal hack: 
While starting the development, the OAuth token can be added via the first program argument with no ``oauth:`` prefix on it.

## Configuration properties

You must set these configuration properties by overriding either ``application.yml`` or as environment variables before running the bot.

OAuth configuration:

- ``twitch.oauth.client-id``: Twitch API OAuth client id
- ``twitch.oauth.client-secret``: Twitch API OAuth client secret

IRC configuration:

- ``twitch.irc.host:`` IRC server host. Default value will be ``irc.chat.twitch.tv`` if not set.
- ``twitch.irc.nick:`` Bot nick
- ``twitch.irc.channel:`` Channel to join without hashtag (#)

