# GitHub Repository Search App

このアプリケーションは、GitHubのリポジトリを検索できるAndroidアプリケーションです。

## 機能

- リポジトリの検索機能
- 検索結果の表示（リポジトリ名、説明、スター数など）
- ダークモード対応
- モダンなMaterial Design 3のUI

## 技術スタック

- **言語**: Kotlin
- **アーキテクチャ**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **非同期処理**: Kotlin Coroutines, Flow
- **ネットワーク**: Retrofit2, OkHttp3
- **JSON解析**: Moshi
- **画像読み込み**: Coil

## 実装した改善点

### 1. UI/UXの改善
- Material Design 3に準拠したモダンなUIの実装
- TopAppBarの追加によるナビゲーションの改善
- ダークモード対応による視認性の向上

### 2. コードの品質改善
- ViewModelでのエラーハンドリングの実装
- Flowを使用したリアクティブな状態管理
- コードの可読性向上のためのドキュメンテーション追加

### 3. セキュリティ対策
- GitHub Personal Access Tokenの安全な管理
- local.propertiesを使用した機密情報の保護

## セットアップ手順

1. プロジェクトをクローン
```bash
git clone [repository-url]
```

2. GitHub Personal Access Tokenの設定
   - GitHubで新しいPersonal Access Tokenを作成
   - `local.properties`ファイルに以下を追加:
```properties
GITHUB_TOKEN=your_github_token_here
```

3. Android Studioでプロジェクトを開く

4. プロジェクトをビルドして実行

## 既知の課題

1. レイアウトの改善点
   - 検索バーの位置調整
   - エラー表示の改善
   - ローディング表示の最適化

2. エラーハンドリング
   - ネットワークエラーの詳細表示
   - リトライ機能の追加

3. パフォーマンス
   - 大量データ時の表示最適化
   - メモリリークの防止

## コントリビューション

1. このリポジトリをフォーク
2. 新しいブランチを作成 (`git checkout -b feature/amazing-feature`)
3. 変更をコミット (`git commit -m 'Add some amazing feature'`)
4. ブランチにプッシュ (`git push origin feature/amazing-feature`)
5. Pull Requestを作成

## ライセンス

[MIT License](LICENSE)
