# questions-bot

## Program arguments

Temporal hack: 
While starting the development, the OAuth token can be added via the first program argument with no ``oauth:`` prefix on it.

## Configuration properties

You must set these configuration properties by overriding either ``application.yml`` or as environment variables before running the bot.

Twitch OAuth 2.0 configuration:

- ``spring.security.oauth2.client.registration.twitch.client-id``: Twitch API OAuth client id
- ``spring.security.oauth2.client.registration.twitch.client-secret``: Twitch API OAuth client secret

IRC configuration:

- ``twitch.irc.host``: (optional) IRC server host. Default value will be ``irc.chat.twitch.tv`` if not set.
- ``twitch.irc.nick``: Bot nick (lowercase)
- ``twitch.irc.channel``: Channel to join without hashtag (#)


Resource Server OAuth 2.0 configuration:

- ``okta-client-id``: Okta OAuth client id
- ``okta-client-secret``: Okta OAuth client secret
- ``okta-domain``: Okta Authorization Server domain prefix.

