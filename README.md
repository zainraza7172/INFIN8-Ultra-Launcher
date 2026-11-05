# INFIN8 Ultra Launcher

An original Android home-screen launcher prototype with a premium dark interface, installed-app discovery, app search, and app launching.

## Compatibility
- Android 8.0 (API 26) and newer
- Designed for phones including Samsung Galaxy A07
- Does not require root, Shizuku, Good Lock, or Accessibility access

## Build an APK with GitHub Actions
1. Create a GitHub repository named `INFIN8-Ultra-Launcher`.
2. Upload the contents of this project to the repository root (not the ZIP file itself).
3. Open **Actions** → **Build INFIN8 Ultra APK**.
4. Select **Run workflow**.
5. When it finishes, open the workflow run and download the `INFIN8-Ultra-debug` artifact.
6. Extract the ZIP artifact; install `app-debug.apk` on your phone.

A push to `main` or `master` that changes app source also triggers a build.

## Set as default launcher
After installation, press Home. Android should offer a Home app chooser. Select **INFIN8 Ultra** and choose **Always**. You can switch back to One UI Home in Settings → Apps → Default apps → Home app.

## Current scope
This is Version 0.1.0: home role, app drawer grid, search, and app launching. Widgets, folders, gestures, icon packs, backup/restore, and release signing are planned for later versions.

## Safety
The app does not request network, contacts, location, SMS, phone, or accessibility permissions. It does not replace Samsung firmware or modify system partitions.
