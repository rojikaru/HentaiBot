# HentaiBot

A Telegram bot written in Java that serves anime and NSFW content from various APIs.

## Features

- Fetch and serve content from multiple sources:
  - Rule34 API for NSFW content
  - Waifu API for both SFW and NSFW anime content
- Support for multiple media formats:
  - Static images
  - GIF animations
  - Video clips
- Easy-to-use commands

## Prerequisites

- Java 21 or higher
- Telegram Bot API Token
- Maven (for dependency management)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/rojikaru/hentaibot.git
cd hentaibot
```

2. Configure your bot token:
   - Create a `application.properties` file in the `src/main/resources` directory
   - Add your Telegram bot token:
     ```properties
     BOT_TOKEN=YOUR_BOT_TOKEN_HERE
     BOT_NAME=YOUR_BOT_USERNAME
     ```

3. Build the project:
```bash
mvn clean package
```

## Usage

1. Start the bot:
```bash
java -jar target/hentaibot-1.0.jar
```

2. In Telegram, start a chat with your bot and use the following commands:
   - `/start` - Initialize the bot
   - `/help` - Display available commands
   - `/sfw` - Get SFW anime content
   - `/nsfw` - Get NSFW content (age verification required)
   - `/tags [tag1 tag2...]` - Search content by tags

## Configuration

The bot can be configured through the `application.properties` file:

```properties
# API Settings
RULE_34_API=https://api.rule34.xxx/
WAIFU_API=https://api.waifu.pics/
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Disclaimer

This bot serves NSFW content and is intended for adults only. Please ensure compliance with your local laws and Telegram's terms of service.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
