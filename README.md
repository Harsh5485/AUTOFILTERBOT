# RV-Bot Fixed by Aryan

A powerful Telegram bot for downloading DRM-protected content from various streaming platforms, including HotStar.

## Features

- Download DRM-protected content from HotStar
- Support for movies and TV shows
- Multiple quality options
- Audio track selection
- Automatic decryption and merging
- Google Drive upload capability

## Configuration

Create a `config.env` file in the root directory with the following variables:

```
BOT_TOKEN=your_bot_token_here
API_ID=your_api_id_here
API_HASH=your_api_hash_here
OWNER_ID=your_telegram_user_id
DB_URL=your_mongodb_connection_string
LOG_CHANNEL=your_log_channel_id
GDRIVE_FOLDER_ID=your_google_drive_folder_id
INDEX_LINK=your_google_drive_index_link
HOTSTAR_USER_TOKEN=your_hotstar_user_token
HOTSTAR_DEVICE_ID=your_hotstar_device_id
```

## How to Use HotStar Downloads

### 1. Basic Usage
Send the following command to your bot:
```
/hs [HotStar_URL]
```

Example:
```
/hs https://www.hotstar.com/in/movies/uriyadi/1260050861
```

### 2. For TV Shows
Specify season and episode:
```
/hs https://www.hotstar.com/in/shows/example/123456789/seasons/1/episodes/1-5
```

### 3. Quality Selection
After sending the command:
1. The bot will fetch available qualities and audio tracks
2. Select your preferred video quality
3. Choose desired audio languages
4. Click "DONE" to start the download

### 4. Codec Options
You can specify video codecs by appending them to the command:
```
/hs [URL] x264    # for H.264 codec
/hs [URL] x265    # for H.265/HEVC codec
```

## Deployment

### Deploy on Koyeb

This bot is ready for deployment on Koyeb. The repository includes:

- `Dockerfile` for containerization
- `koyeb.yaml` for Koyeb-specific configuration
- `Procfile` for worker process

To deploy on Koyeb:

1. Fork this repository
2. Connect your GitHub repository to Koyeb
3. Set the environment variables in Koyeb dashboard
4. Deploy!

### Local Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/RV-Bot-Fixed-By-Aryan.git
cd RV-Bot-Fixed-By-Aryan
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Configure the bot (see Configuration section)

4. Run the bot:
```bash
python bot.py
```

## Requirements

- Python 3.8+
- Valid Telegram Bot Token
- Telegram API ID and Hash
- HotStar subscription with valid tokens
- MongoDB connection string (for user data)
- Google Drive API credentials (optional)

## Note

This bot is intended for personal use only. Respect the terms of service of streaming platforms and only download content you have rights to access.