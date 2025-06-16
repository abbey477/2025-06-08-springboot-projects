# Setting Up GitHub Credential Manager on Ubuntu

## What is GitHub Credential Manager?

GitHub Credential Manager (GCM) saves your GitHub login information so you don't have to type your username and password every time you work with GitHub repositories.

## What You Need

- Ubuntu 18.04 or newer
- Git installed on your computer
- A GitHub account

## Step 1: Install Git (if not already installed)

```bash
sudo apt update
sudo apt install git
```

## Step 2: Download GitHub Credential Manager

Open your terminal and run:

```bash
cd ~/Downloads
wget https://github.com/git-ecosystem/git-credential-manager/releases/latest/download/gcm-linux_amd64.2.0.877.deb
```

## Step 3: Install GitHub Credential Manager

```bash
sudo dpkg -i gcm-linux_amd64.2.0.877.deb
```

## Step 4: Configure Git to Use GCM

```bash
git-credential-manager configure
```

## Step 5: Create a Personal Access Token on GitHub

1. Go to GitHub.com and log in
2. Click your profile picture → **Settings**
3. Scroll down and click **Developer settings**
4. Click **Personal access tokens** → **Tokens (classic)**
5. Click **Generate new token (classic)**
6. Give it a name (like "My Ubuntu Computer")
7. Select **repo** checkbox for repository access
8. Click **Generate token**
9. **Copy the token** (you won't see it again!)

## Step 6: Test Your Setup

Try cloning a repository or any Git operation:

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
```

When prompted:
- **Username**: Your GitHub username
- **Password**: Paste your personal access token (NOT your GitHub password)

## That's It!

GCM will remember your credentials. You won't need to enter them again for future Git operations.

## Quick Commands Reference

Check if GCM is working:
```bash
git-credential-manager --version
```

Remove saved credentials (if needed):
```bash
echo "url=https://github.com" | git-credential-manager erase
```

## Troubleshooting

**Problem**: `git-credential-manager: command not found`

**Solution**: Restart your terminal or run:
```bash
source ~/.bashrc
```

**Problem**: Still asking for password every time

**Solution**: Make sure you used your **personal access token** as the password, not your GitHub account password.

---

**Important**: Always use your **Personal Access Token** as the password, never your actual GitHub password!